package pers.jz.unittest.test;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Jemmy Zhang on 2018/4/25.
 */
public class ExceptionTest {

    @Test(expected = RuntimeException.class)
    public void testRuntimeException() {
        throw new RuntimeException();
    }

    @Test(expected = Exception.class)
    public void testException() throws IOException {
        throw new IOException();
    }

    @Test(expected = IOException.class)
    public void testIoExceptionTest() throws Exception{
        throw new FileNotFoundException();
    }

    @Test(expected = IOException.class)
    public void testFateRuntimeException(){
        throw new RuntimeException();
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException()throws IOException{
        throw new IOException();
    }
}
