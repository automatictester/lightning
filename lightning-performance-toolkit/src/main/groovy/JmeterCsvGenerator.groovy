class JmeterCsvGenerator {

    static void main(String[] args) {
        println "timeStamp,elapsed,label,success";
        for (int i = 0; i < args[0].toInteger(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(getTimeStamp()).append(",").append(getElapsed()).append(",").append(getLabel()).append(",").append(isSuccess());
            println sb.toString();
        }
    }

    private static String getTimeStamp() {
        return (new Random().nextInt(3600) + 1472472000).toString();
    }

    private static String getElapsed() {
        return (new Random().nextInt(500) + 1).toString();
    }

    private static String getLabel() {
        String [] labels = ["Login (user)", "Login (admin)", "Logout", "Standing order setup", "Transfer", "One-off payment", "Trasaction search", "FAQ", "Personal details update", "Personal details display"];
        int i = new Random().nextInt(9);
        return labels[i];
    }

    private static String isSuccess() {
        int x = new Random().nextInt(9);
        if (x == 0) {
            return "false";
        } else {
            return "true";
        }
    }
}