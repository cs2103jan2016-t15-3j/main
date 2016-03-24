package feedback;

public class BreakRegion {
    
    private final String[] KEY_WORDS = {"on", "by", "from"};
    
    private String description;
    private String dateRegion;
    private String tagRegion;
    private String keyWord;
    
    public BreakRegion(String userInput) {
        description = "";
        dateRegion = "";
        tagRegion = "";
        keyWord = ""; 
        
        int keyWordPosition = getKeyWOrdPosition(userInput);
        int tagPosition;
        if (keyWordPosition > 0) {
            description = userInput.substring(0, keyWordPosition);
            keyWord = findKeyWord(userInput).trim();
            
            String afterKeyWord = userInput.substring(keyWordPosition + keyWord.length());
            tagPosition = getTagPosition(afterKeyWord);
            if (tagPosition > 0) {
                dateRegion = afterKeyWord.substring(0, tagPosition).trim();
                tagRegion = afterKeyWord.substring(tagPosition + 1);
            } else {
                dateRegion = afterKeyWord.trim();
            }
        } else {
            tagPosition = getTagPosition(userInput);
            if (tagPosition > 0) {
                description = userInput.substring(0, tagPosition).trim();
                tagRegion = userInput.substring(tagPosition + 1);
            } else {
                description = userInput.trim();
            }
        }
        
        System.out.println();
        System.out.println("BreakRegion------------------------------------------");
        System.out.println("Description: " + description + "|");
        System.out.println("DateRegion: " + dateRegion + "|");
        System.out.println("TagRegion: " + tagRegion + "|");
        System.out.println("KeyWord: " + keyWord + "|");
        System.out.println("------------------------------------------BreakRegion");
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getDateRegion() {
        return dateRegion;
    }
    
    public String getTagRegion() {
        return tagRegion;
    }
    
    public String getKeyWord() {
        return keyWord;
    }
    
    private int getKeyWOrdPosition(String input) {
        String keyWord = findKeyWord(input);
        if (!keyWord.isEmpty()) {
            return input.lastIndexOf(keyWord);
        } else {
            return -1;
        }
    }
    
    private int getTagPosition(String input) {
        if (input.contains(" #")) {
            return input.indexOf(" #");
        } else {
            return -1;
        }
    }
    
    private String findKeyWord(String userInput) {
        int position = 0;
        String keyWord = "";
        if (userInput.contains(new String(" " + KEY_WORDS[0] + " ")) && userInput.lastIndexOf(KEY_WORDS[0]) > position) {
            position = userInput.lastIndexOf(KEY_WORDS[0]);
            keyWord = KEY_WORDS[0];
        }
        if (userInput.contains(new String(" " + KEY_WORDS[1] + " ")) && userInput.lastIndexOf(KEY_WORDS[1]) > position) {
            position = userInput.lastIndexOf(KEY_WORDS[1]);
            keyWord = KEY_WORDS[1];
        }
        if (userInput.contains(new String(" " + KEY_WORDS[2] + " ")) && userInput.lastIndexOf(KEY_WORDS[2]) > position) {
            position = userInput.lastIndexOf(KEY_WORDS[2]);
            keyWord = KEY_WORDS[2];
        }
        System.out.println("Keyword = " + keyWord);
        return keyWord;
    }
}
