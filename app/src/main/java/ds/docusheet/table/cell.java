package ds.docusheet.table;

public class cell {
    String data;
    int doc_id;

    public cell(String data) {
        this.data = data;

    }

    public cell(String data, int doc_id) {
        this.data = data;
        this.doc_id = doc_id;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public void setData(String data) {
        this.data = data;
    }



    public String getData() {
        return data;
    }


}
