package common.response;

public class CommandsResponse extends Response{
    public String line;
    public CommandsResponse(String name, String error, String result) {
        super(name, error, result);
    }
   // public CommandsResponse(String name, String error, String result,String line) {
  //      super(name, error, result);
  //      this.line=line;
   // }


}
