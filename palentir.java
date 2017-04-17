import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/*
 * Nate Bhurinat W. (nate.bwangsut@gmail.com)
 * [@natebwangsut] https://github.com/natebwangsut
 *
 * Palentir Coding Challenge
 *
 * Advise: Minutes of planning & reading can save days of coding (I learnt it the hard way).
 */

public class palentir {
    public static void main(String[] args) {
        String[] feed1 = {
            "0|1000",
            "0|Shilpa|BUY|30000",
            "0|Will|BUY|50000",
            "0|Tom|BUY|40000",
            "0|Kristi|BUY|15000",
            "1|Kristi|BUY|11000",
            "1|Tom|BUY|1000",
            "1|Will|BUY|19000",
            "1|Shilpa|BUY|25000",
            "2|1500",
            "2|Will|SELL|7000",
            "2|Shilpa|SELL|8000",
            "2|Kristi|SELL|6000",
            "2|Tom|SELL|9000",
            "3|500",
            "38|1000",
            "78|Shilpa|BUY|30000",
            "79|Kristi|BUY|60000",
            "80|1100",
            "81|1200"
        };

        String[] feed2 = {
            "0|20",
            "0|Kristi|SELL|3000",
            "0|Will|BUY|5000",
            "0|Tom|BUY|50000",
            "0|Shilpa|BUY|1500",
            "1|Tom|BUY|1500000",
            "3|25",
            "5|Shilpa|SELL|1500",
            "8|Kristi|SELL|600000",
            "9|Shilpa|BUY|500",
            "10|15",
            "11|5",
            "14|Will|BUY|100000",
            "15|Will|BUY|100000",
            "16|Will|BUY|100000",
            "17|25"
        };

        System.out.println(Arrays.toString(catchingInsider(feed1)));
        System.out.println(Arrays.toString(catchingInsider(feed2)));
    }

    public static String[] catchingInsider(String[] datafree) {

        ArrayList<String> insider = new ArrayList<String>();
        ArrayList<String> flagged = new ArrayList<String>();
        HashMap<String, ArrayList<Trade>> trades = new HashMap<String, ArrayList<Trade>>();

        int day = -1;
        int price = -1;
        String person = "";
        String action = "";
        int amount = -1;

        int THRESHOLD = 500000;
        int DAY_THRESHOLD = 3;

        for (String line : datafree) {

            String[] token = line.split("\\|");
            day = Integer.parseInt(token[0]);

            if (token.length > 2) {
                person = token[1];
                action = token[2];
                amount = Integer.parseInt(token[3]);

                // Early exit if the person is already flagged
                if(flagged.contains(person)) { continue; }

                // Create new ArrayList object if null
                trades.putIfAbsent(person, new ArrayList<Trade>());
                trades.get(person).add(new Trade(day, action, amount, price));
            } else {
                price = Integer.parseInt(token[1]);
            }

            // Iterate over person's transaction
            Iterator<String> keys = trades.keySet().iterator();
            while (keys.hasNext()) {
                person = keys.next();

                // For each trade
                Iterator<Trade> trade = trades.get(person).iterator();
                while (trade.hasNext()) {

                    // Remove trade if less than minimum time
                    Trade t = trade.next();
                    if (day - t.day >= DAY_THRESHOLD) {
                        trade.remove();
                        continue;
                    }

                    amount = 0;
                    if (t.action.equals("BUY")) {
                        amount = (price - t.price) * t.amount;
                    } else {
                        amount = (t.price - price) * t.amount;
                    }

                    if (amount >= THRESHOLD && !flagged.contains(person)) {
                        flagged.add(person);
                        insider.add(String.valueOf(t.day));
                    }
                }
            }
        }

        // Output formatting
        ArrayList<String> returnStrings = new ArrayList<String>();
        Iterator<String> retDays = insider.iterator();
        Iterator<String> retPers = flagged.iterator();

        while (retDays.hasNext() && retPers.hasNext()) {
            returnStrings.add(retDays.next() + "|" + retPers.next());
        }

        return returnStrings.toArray(new String[returnStrings.size()]);
    }

    public static class Trade {

        public int day;
        public String action;
        public int amount;
        public int price;
        public Trade(int day, String action, int amount, int price) {
            this.day = day;
            this.action = action;
            this.amount = amount;
            this.price = price;
        }
    }
}