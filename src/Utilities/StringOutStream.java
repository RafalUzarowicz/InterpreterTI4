package Utilities;

public class StringOutStream implements OStream{
    private final StringBuilder stream;
    private final boolean isPrinting;
    public StringOutStream(boolean isPrinting){
        stream = new StringBuilder();
        this.isPrinting = isPrinting;
    }

    public StringOutStream(){
        stream = new StringBuilder();
        this.isPrinting = true;
    }

    public void print(String string){
        stream.append(string);
        if(isPrinting){
            System.out.println(string);
        }
    }

    public StringBuilder getStream() {
        return stream;
    }
}
