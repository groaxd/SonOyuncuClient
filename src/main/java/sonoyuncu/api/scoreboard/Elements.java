package sonoyuncu.api.scoreboard;

public enum Elements {
   UPDATE_VISIBILITY,
   SET_ELEMENTS,
   ADD_ELEMENTS,
   UPDATE_ELEMENTS,
   UPDATE_INDEXES,
   REMOVE_ELEMENTS,
   CLEAR_ELEMENTS;


   public static Elements get(int var0) {
      return values()[var0];
   }
}
