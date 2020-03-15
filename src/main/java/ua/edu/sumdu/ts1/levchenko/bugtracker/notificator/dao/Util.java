package ua.edu.sumdu.ts1.levchenko.bugtracker.notificator.dao;

class Util {
    public static boolean noneIsEmpty(String... s) {
        for (String s1 : s) {
            if (!s1.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static String[] splitId(String id) throws Exception {
        String[] parts = id.split("-");
        if (parts.length < 2) {
            throw new Exception("Failed to split id");
        }
        return parts;
    }
}
