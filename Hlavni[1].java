package httpBenchMark;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
 
class mojeVlakno extends Thread {
    static int req_cnt, port;
    static String host;
 
    public void run() {
        System.out.println("Vlakno bezi.");
        int counter = 0;
        try (Socket s = new Socket(host, port)) {
            InputStream in = s.getInputStream();
            OutputStream out = s.getOutputStream();
            for (int i = 0; i < req_cnt; i++) {
                out.write(("GET / HTTP/1.0\r\n" + 
                        "Connection: keep-alive\r\n\r\n"
                        ).getBytes());
                out.flush();
            }
            int pocet;
            byte buffer[] = new byte[2048];
            while ((pocet = in.read(buffer)) != -1) {
                counter += pocet;
                System.out.write(buffer, 0, pocet);
                System.out.flush();
            }
//          s.close();
            System.out.println("Vlakno skoncilo, precteno bajtu: " + counter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 
public class Hlavni {
 
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        if (args.length < 4) {
            System.err.println("Ocekavam 4 parametry:" + " host port threads request_count");
            return;
        }
        mojeVlakno.host = args[0];
        mojeVlakno.port = Integer.parseInt(args[1]);
        int threads = Integer.parseInt(args[2]);
        mojeVlakno.req_cnt = Integer.parseInt(args[3]);
        for (int i = 0; i < threads; i++) {
            mojeVlakno v = new mojeVlakno();
            v.start();
        }
    }
 
}