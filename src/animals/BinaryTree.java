package animals;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BinaryTree {
    BaseTreeNode root;

    public BinaryTree(BaseTreeNode root) {
        this.root = root;
    }

    public void replaceParent(BaseTreeNode node, BaseTreeNode oldParent, BaseTreeNode parent) {
        if (root == node || parent instanceof AnimalTreeNode) {
            root = parent;
        } else {
            ((StatementTreeNode)oldParent).replace(node, (StatementTreeNode) parent);
        }
    }

    private ObjectMapper newMapper(FileType fileType) {
        switch (fileType) {
            case JSON:
                return new JsonMapper();
            case XML:
                return new XmlMapper();
            case YAML:
                return new YAMLMapper();
            default:
                return null;
        }
    }

    boolean load(FileType fileType, String suffix) {
        File file;
        if (suffix.equals("en")) {
            file = new File("animals." + fileType.extension());
        } else {
            file = new File("animals_" + suffix + "." + fileType.extension());
        }
        if (!file.exists()) {
            return false;
        }
        ObjectMapper objectMapper = newMapper(fileType);
//        System.out.println("Try to read file: " + file.getAbsolutePath());
        try {
            root = objectMapper
                    .activateDefaultTyping(new PolymorphicTypeValidator() {
                        @Override
                        public Validity validateBaseType(MapperConfig<?> config, JavaType baseType) {
                            return Validity.ALLOWED;
                        }

                        @Override
                        public Validity validateSubClassName(MapperConfig<?> config, JavaType baseType, String subClassName) {
                            return Validity.ALLOWED;
                        }

                        @Override
                        public Validity validateSubType(MapperConfig<?> config, JavaType baseType, JavaType subType) {
                            return Validity.ALLOWED;
                        }
                    }, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE)
//                    .enableDefaultTyping()
                    .readValue(file, BaseTreeNode.class);
            return true;
        } catch (JsonParseException | JsonMappingException e) {
            e.printStackTrace();

        } catch (IOException ignored) {
        }
        return false;
    }

    void save(FileType fileType, String suffix) {
        ObjectMapper objectMapper = newMapper(fileType);
        try {
            File file;
            if (suffix.equals("en")) {
                file = new File("animals." + fileType.extension());
            } else {
                file = new File("animals_" + suffix + "." + fileType.extension());
            }

            objectMapper
                    .activateDefaultTyping(new PolymorphicTypeValidator() {
                        @Override
                        public Validity validateBaseType(MapperConfig<?> config, JavaType baseType) {
                            return Validity.ALLOWED;
                        }

                        @Override
                        public Validity validateSubClassName(MapperConfig<?> config, JavaType baseType, String subClassName) {
                            return Validity.ALLOWED;
                        }

                        @Override
                        public Validity validateSubType(MapperConfig<?> config, JavaType baseType, JavaType subType) {
                            return Validity.ALLOWED;
                        }
                    }, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE)
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(file, root);
            System.out.println("Saved file: " + file.getAbsolutePath());
        } catch (IOException ignored) {
        }
    }

    public Set<String> animals() {
        Set<String> animals = new TreeSet<>();
        if (root != null) {
            root.listAnimals(animals);
        }
        return animals;
    }

    public List<String> search(String animalName) {
        if (root != null) {
            return root.search(animalName);
        } else {
            return null;
        }
    }

    public TreeStatistics statistics() {
        if (root == null) {
            return null;
        }
        TreeStatistics stats = new TreeStatistics();

        stats.rootStr = root.toString();

        root.updateStatistics(stats, 0);
        return stats;
    }

    public void print() {
        if (root != null) {
            root.print(0);
        }
    }
}
