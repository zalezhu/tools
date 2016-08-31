package cn.com.cardinfo.cmdcall.command;

import cn.com.cardinfo.cmdcall.model.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Zale on 16/8/20.
 */
public class ShellCommand implements Command {
    private String command;

    public ShellCommand(String command) {
        this.command = command;
    }

    public Result toDo() {
        Result<String> rst = new Result<>();
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (InputStream fis = process.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
                String line = null;
                StringBuffer cmdout = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    cmdout.append(line).append(System.getProperty("line.separator"));
                }
                rst.setObj(cmdout.toString());
                rst.setHasError(false);
                return rst;
            }
        }catch (IOException e){
            rst.setHasError(true);
            rst.setError(e.getMessage());
            return rst;
        }

    }
}
