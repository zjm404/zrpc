package github.zjm404.zrpc.protocol.serialization;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import github.zjm404.zrpc.core.ISerialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zjm
 * @date 2021/2/25
 */
public class HessianSerialization implements ISerialization {
    @Override
    public <T> byte[] enSerialize(T obj){
        if(obj == null){
            throw  new NullPointerException();
        }
        HessianSerializerOutput hessianOutput;
        byte[] data = new byte[0];
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
            hessianOutput = new HessianSerializerOutput(os);
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            data = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clz){
        if(bytes == null || bytes.length == 0){
            throw new NullPointerException();
        }
        T obj = null;
        try(ByteArrayInputStream is = new ByteArrayInputStream(bytes)){
            HessianSerializerInput hessianInput = new HessianSerializerInput(is);
            obj = (T) hessianInput.readObject(clz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
