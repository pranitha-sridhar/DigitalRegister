package ds.docusheet.table;

public class Cards {
    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public Cards(String cards, int img) {
        this.cards = cards;
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    private String cards;
    private int img;

}
