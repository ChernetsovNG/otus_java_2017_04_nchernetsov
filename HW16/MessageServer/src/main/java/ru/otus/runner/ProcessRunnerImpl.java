package ru.otus.runner;

import ru.otus.app.ProcessRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

// Запускает отдельный процесс (JVM)
public class ProcessRunnerImpl implements ProcessRunner {
    private static final Logger LOG = Logger.getLogger(ProcessRunnerImpl.class.getName());

    private final StringBuffer out = new StringBuffer();
    private Process process;

    public void start(String command) throws IOException {
        process = runProcess(command);
    }

    public void stop() {
        process.destroy();
    }

    public String getOutput() {
        return out.toString();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        Process p = pb.start();

        LOG.info("Start process: " + p + "(isAlive = " + p.isAlive() + ")" + " by command: " + command);

        //StreamListener errors = new StreamListener(p.getErrorStream(), "ERROR");
        //StreamListener output = new StreamListener(p.getInputStream(), "OUTPUT");

        //output.start();
        //errors.start();
        return p;
    }

    private class StreamListener extends Thread {
        private final Logger logger = Logger.getLogger(StreamListener.class.getName());

        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(type).append('>').append(line).append('\n');
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }
}
