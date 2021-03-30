package ds.docusheet.table;

public class ColumnData {
    private String column_names;
    private String column_type;
    private String id;
    private String column_nums;
    private String row_nums;

    public ColumnData() {
    }

    public ColumnData( String id,String column_names, String column_type, String column_nums, String row_nums) {
        this.column_names = column_names;
        this.column_type = column_type;
        this.id = id;
        this.column_nums = column_nums;
        this.row_nums = row_nums;
    }

    public String getColumn_names() {
        return column_names;
    }

    public void setColumn_names(String column_names) {
        this.column_names = column_names;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumn_nums() {
        return column_nums;
    }

    public void setColumn_nums(String column_nums) {
        this.column_nums = column_nums;
    }

    public String getRow_nums() {
        return row_nums;
    }

    public void setRow_nums(String row_nums) {
        this.row_nums = row_nums;
    }
}
