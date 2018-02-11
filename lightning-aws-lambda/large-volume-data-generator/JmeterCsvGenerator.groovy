class JmeterCsvGenerator {

    static void main(String[] args) {
        println "timeStamp,elapsed,label,success";
        for (int i = 0; i < args[0].toInteger(); i++) {
            StringBuilder sb = new StringBuilder()
            sb.append(getTimeStamp()).append(",").append(getElapsed()).append(",").append(getLabel()).append(",").append(isSuccess())
            println sb.toString()
        }
    }

    static String getTimeStamp() {
        new Random().nextInt(3600) + 1472472000
    }

    static String getElapsed() {
        new Random().nextInt(500) + 1
    }

    static String getLabel() {
        String [] labels = ["Login (user)", "Login (admin)", "Logout", "Standing order setup", "Transfer", "One-off payment", "Trasaction search", "FAQ", "Personal details update", "Personal details display"]
        int randomLabelId = new Random().nextInt(9)
        labels[randomLabelId]
    }

    static String isSuccess() {
        new Random().nextInt(9) != 0
    }
}