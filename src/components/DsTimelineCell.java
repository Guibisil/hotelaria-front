package components;

public class DsTimelineCell {
    private String id;
    private boolean isStart;
    private boolean isEnd;
    private String text;

    public DsTimelineCell(String id, boolean isStart, boolean isEnd, String text) {
        this.id = id;
        this.isStart = isStart;
        this.isEnd = isEnd;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public boolean isStart() {
        return isStart;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public String getText() {
        return text;
    }
}
