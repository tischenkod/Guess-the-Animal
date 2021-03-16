package animals;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

public class Main {

    public static void main(String[] args) {
        FileType fileType;

        if (args.length == 2 && args[0].equals("-type")) {
            switch (args[1]) {
                case "xml":
                    fileType = FileType.XML;
                    break;
                case "yaml":
                    fileType = FileType.YAML;
                    break;
                default:
                    fileType = FileType.JSON;
            }
        } else {
            fileType = FileType.JSON;
        }

        try {
            Game game = new Game(fileType);
            game.run();
        } catch (InstanceAlreadyExistsException | InstanceNotFoundException e) {
            e.printStackTrace();
        }
    }
}
