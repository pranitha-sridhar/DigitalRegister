package ds.docusheet.table;

public class Document {
    private String doc_id;
    String doc_name;
    String update;

    public String getDoc_id() {
        return doc_id;
    }
    public String getDoc_name() {
        return doc_name;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }
    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public Document() {
    }

    public Document(String doc_id,String doc_name,String update) {
        this.doc_id = doc_id;
        this.doc_name=doc_name;
        this.update=update;
    }
}
