import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellTool {
    /**
     * 日志工具。
     */
    private final static Logger logger = LoggerFactory.getLogger(ShellTool.class);

    /**
     *
     * @param cmd
     * @param dir
     * @return
     */
    public static String exec (String cmd,File dir){
        logger.info("Start execCmd :{}",cmd);
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        String[] commond = {"/bin/bash","-c",cmd};
        // 执行命令, 返回一个子进程对象（命令在子进程中执行）
        try {
            if (null !=dir) {
                process = Runtime.getRuntime().exec(commond, null, dir);
            }else{
                process = Runtime.getRuntime().exec(commond);
            }
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            try( InputStreamReader in = new InputStreamReader(process.getInputStream(), "UTF-8");
                 InputStreamReader in1 = new InputStreamReader(process.getErrorStream(), "UTF-8");) {
                bufrIn = new BufferedReader(in);
                bufrError = new BufferedReader(in1);
                // 读取输出
                String line = null;
                while ((line = bufrIn.readLine()) != null) {
                    result.append(line).append('\n');
                }
                while ((line = bufrError.readLine()) != null) {
                    result.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            logger.error("execCmd:{} Error:{}",cmd,e);
        } catch (InterruptedException e) {
            logger.error("execCmd:{} Error:{}",cmd,e);
        }finally {
            process.destroy();
        }
        logger.info("exec cdm:{},result is :{}",cmd,result.toString());
        return result.toString();
    }
}
