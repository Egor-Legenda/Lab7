package org.example.common.response;

public class CommandsResponse extends Response{
    public String line;
    public int id;
    public boolean add;
    public CommandsResponse(String name, String error, String result) {
        super(name, error, result);
    }
    public CommandsResponse(String name, String error, String result, int id, boolean add ) {
        super(name, error, result);
        this.add=add;
        this.id=id;
    }

    public String getLine() {
        return line;
    }

    public int getId() {
        return id;
    }

    public boolean isAdd() {
        return add;
    }
// public CommandsResponse(String name, String error, String result,String line) {
  //      super(name, error, result);
  //      this.line=line;
   // }


}
