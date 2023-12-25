package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Example {

    private final String title;
    private final String authorName;
    private final int pageCount;
    private final String[] mainCharacters;



    @JsonCreator

    public Example(@JsonProperty("title") String title,
                   @JsonProperty("authorName") String authorName,
                   @JsonProperty("pageCount") int pageCount,
                   @JsonProperty("chapters") String[] mainCharacters) {
        this.title = title;
        this.authorName = authorName;
        this.pageCount = pageCount;
        this.mainCharacters = mainCharacters;

    }

    public String getTitle(){
        return title;
    }


    public String getAuthorName() {
        return authorName;
    }

    public int getPageCount() {
        return pageCount;
    }


    public String[] getMainCharacters() {
        return mainCharacters;
    }
}
