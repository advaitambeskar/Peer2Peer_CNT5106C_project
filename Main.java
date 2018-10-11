public class Main {

    public static void main(String[] args) throws Exception {
        final Config config = new Config();
        final Logger logger = new Logger(config);
        final Server server = new Server(config, logger);
    }
}