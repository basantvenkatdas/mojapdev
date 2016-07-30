package mojap108.mojap;

/**
 * Created by gollaba on 7/29/16.
 */
public class ProfileDrawerListItem {
    private String text;
    private int imageResource;
    private int type;

    public ProfileDrawerListItem(String text, int imageResource,int type) {
        this.text = text;
        this.imageResource = imageResource;
        this.type = type;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int resource) {
        this.imageResource = resource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
