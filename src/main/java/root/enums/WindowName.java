package root.enums;

public enum WindowName {


    TORWAK_MARTIN("EVE - Torwak Martin"),
    TARA_IKKALA("EVE - Tara Ikkala");

    private String title;

    WindowName(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
