package makamys.coretweaks.optimization.transformercache.lite;

import java.util.HashMap;
import java.util.Map;

import com.google.common.hash.Hashing;

public class TransformerCache {
    public static class TransformerData {
        public String transformerClassName;
        public Map<String, CachedTransformation> transformationMap = new HashMap<>();
        

        @Override
        public String toString() {
            return "";
        }

        public TransformerData(String transformerClassName) {
            this.transformerClassName = transformerClassName;
        }
        
        public TransformerData() {}
        
        public static class CachedTransformation {
            String targetClassName;
            int preLength;
            int preHash;
            int postHash;
            byte[] newClass;
            
            @Override
            public String toString() {
                return "";
            }

            public CachedTransformation() {}
            
            public CachedTransformation(String targetClassName, int preHash, int preLength) {

            }
            
            public void putClass(byte[] result) {

            }
        }
    }
}
