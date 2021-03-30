package ds.docusheet.table;

import java.util.List;

public class Templates {
    private String heading;

    private List<Cards> list;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<Cards> getList() {
        return list;
    }

    public void setList(List<Cards> list) {
        this.list = list;
    }

    public Templates(String heading, List<Cards> list) {
        this.heading = heading;
        this.list = list;
    }
}
