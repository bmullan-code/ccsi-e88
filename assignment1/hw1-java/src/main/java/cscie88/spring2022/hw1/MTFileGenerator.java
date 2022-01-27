package cscie88.spring2022.hw1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MTFileGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MTFileGenerator.class);
    private ExecutorService workersThreadPoolService = null;
    private long generationTimeoutInMS = 5000L; //default timeout for data generation in MS

    public MTFileGenerator() {
    }

    // this is the simplest way to run multiple data generators in parallel - it can be improved in many areas;
    // we are waiting up to 'generationTimeoutInMS' milliseconds for completion of all file generators -
    // if it takes more time, the main program will exit before all threads finished their work
    public void runGenerators(int numberOfGenFiles, int numberOfLines){
        // we are creating as many threads as we want to create files - it's not a good idea in general
        // it's better to have a smaller size pool and just re-use threads; but for this example - it's OK
        workersThreadPoolService = Executors.newFixedThreadPool(numberOfGenFiles);
        for (int i=0; i<numberOfGenFiles; i++) {
            String generatorId = "" + i;
            workersThreadPoolService.submit(new RunnableFileGenerator(generatorId, numberOfLines));
        }
        logger.info("runGenerators(): submitted all generators, waiting for completion");
        workersThreadPoolService.shutdown();
        try {
            workersThreadPoolService.awaitTermination(generationTimeoutInMS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.info("Got InterruptedException while shutting down generators, aborting");
        }
        logger.info("runGenerators(): finished waiting for completion");
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java MTFileGenerator <numberOfDataFiles> <numberOfLinesPerFile>");
            System.exit(-1);
        }
        // TODO: should really handle exceptions here
        int numberOfDataFiles = Integer.parseInt(args[0]);
        int numberOfLines = Integer.parseInt(args[1]);
        MTFileGenerator manager = new MTFileGenerator();
        manager.runGenerators(numberOfDataFiles, numberOfLines);
        System.out.println("MTFileGenerator finished OK");
    }

}
