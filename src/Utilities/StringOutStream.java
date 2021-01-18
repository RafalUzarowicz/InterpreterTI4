package Utilities;

/**
 * Author: Rafal Uzarowicz
 * Github: https://github.com/RafalUzarowicz
 */
public class StringOutStream implements IOStream {
    private final StringBuilder stream;
    private String endPrint;
    private final boolean isPrinting;

    public StringOutStream(boolean isPrinting) {
        stream = new StringBuilder();
        this.isPrinting = isPrinting;
    }

    public StringOutStream() {
        stream = new StringBuilder();
        this.isPrinting = true;
    }

    public void print(String string) {
        stream.append(string);
        if (isPrinting) {
            System.out.println(string);
        }
    }

    public void endPrint(String string) {
        endPrint = string;
        if (isPrinting) {
            System.out.println(string);
        }
    }

    public StringBuilder getStream() {
        return stream;
    }
}
