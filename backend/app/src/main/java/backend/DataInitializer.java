//package backend;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    private final DestinationRepository destinationRepository;
//
//    public DataInitializer(DestinationRepository destinationRepository) {
//        this.destinationRepository = destinationRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        destinationRepository.deleteAll(); // 删除所有数据
//        System.out.println("All data deleted.");
//    }
//}
