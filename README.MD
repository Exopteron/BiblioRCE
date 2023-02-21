# An oversight within BiblioCraft which allows restricted server-side file manipulation.


***This method only requires BiblioCraft! Not too much trickery and no other mods are required to achieve code execution!***

# CoreTweaks
The original writeup focused on CoreTweaks can be found [here](CORETWEAKS_METHOD.MD). I recommend you read this first as I may skip some details that will be important below.



## Impact
Code execution is possible through multiple methods.
This affects BiblioCraft 1.7.10 v1.11.7 and BiblioCraft 1.12.2 v2.4.5 (confirmed) and likely all BiblioCraft versions prior to v2.4.6 (unconfirmed, untested)

**Existing patches that fix the path traversal bug will still prevent this new code execution path.**

## Details
This time we are focusing on Vanilla written book saving.

Written books are saved to `world/books/<author-name>, <book-title>`.

The format of these books is, per line:

1. \<book-title\>
2. \<author-name\>
3. \<book-privacy\>

Continuing on from the start, pages of the book are started with a marker `#pgx<page-number>` and from the next line until the next marker is part of that page. BiblioCraft will always add a newline at the end of lines, even if nothing follows.

Like before, we can control both the book title and author name through the book's NBT tag as well as perform path traversal.

## Obtaining Code Execution

The place we are going to write to is the `mods/` folder. Mods in this directory are most often stored as JAR files. A neat property of JAR files is that they are actually just ZIP files in disguise. 

So, why does that help us?

ZIP files have an interesting property where they can still be valid even with garbage data prepended/appended to themselves.

The [EOCD](https://en.wikipedia.org/wiki/ZIP_(file_format)#End_of_central_directory_record_(EOCD)) (End of central directory) record within a ZIP file is placed at the end.

It (roughly) consists of a magic/signature `0x06054b50`, as well as the number, size, and offset of the central directory records within the archive.

The final field in an EOCD record is a length-prefixed comment, which can be almost any byte sequence that isn't the magic *(untested)*.

*(I think ZIP file parsers start from the end and search for the EOCD signature to parse the archive, but I am not entirely sure.)*

If all the ZIP parser needs to find the files within the archive is the EOCD record, and if any data *(with limitations)* can follow the EOCD record, then we can make a valid ZIP even if there is some other data within the file.

### Writing the JAR

*(Huge credit to [https://github.com/c0ny1/ascii-jar](https://github.com/c0ny1/ascii-jar)! I used these tools heavily!)*

First, we need to structure our payload. Forge will load classes as mods if they have the special annotation `@Mod`, so we'll use that here.

*Our rough payload:*
```java
@Mod(modid = "payload-mod")
public class Payload {
    // ...
    static {
        System.out.println("Hello World!");
    }
    // ...
}
```

To avoid encoding issues, we use a script to create an ASCII-only JAR file containing only our payload class.

We pad the beginning with `PK\3\4.jar\n../../mods/\nprivate\n#pgx0\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA`

*The reason we prepend `PK\3\4` (the local file header signature of ZIP) to the padding is due to a strange oddity in Forge that I could not reproduce outside where JAR files are checked to begin with this signature, despite this not being a requirement of ZIP? May be a bug in Forge.*

We use the long string of 'A's and 'a's to make sure the offset is greater than 255, ensuring the 2-byte integer encoding the offset has no bytes outside of ASCII range.


We pad the end with `\n` to avoid problems with BiblioCraft adding newlines.


With our new padded jar, we take all of the bytes following the last newline of the data we padded the beginning with and create a new String from it (we will call this *\<jar-data\>*) and create a new written book with NBT data:

```
  TAG_Compound(''): 2 entries
  {
    TAG_String("author"): "../../mods/"
    TAG_String("title"): "PK\3\4.jar"
    TAG_List("pages"): 1 entry
    {
        TAG_String(None): "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + <jar-data>
    }
  }
```

When we save this book to disk through BiblioCraft, all of the padding data will be "regenerated" by its format, making our JAR valid again.

And now, the next time the server reboots *(even a normal restart, or if you cause a crash)* our mod JAR will be loaded and our code will be executed.

## Proof of concept usage

In `BiblioPOC/tools/` there is a Python3 script to assist in generating a valid payload.

The arguments are:

`python3 create_payload.py [java_file] [forge_jar_location] [class_name] [output_dir]`   

Then, when in-game while holding a BiblioCraft atlas execute
`/jarpoccommand [completed_jar_path]` where `[completed_jar_path]` is the path to `[output_dir]/completed.jar`.