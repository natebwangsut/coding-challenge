"""
Nate Bhurinat W. (nate.bwangsut@gmail.com)
[@natebwangsut] https://github.com/natebwangsut

Palentir Coding Challenge

Advise: Minutes of planning & reading can save days of coding (I learnt it the hard way).
"""

def catching_insider(datafeed):
    """Return a list of insider with the respective day that expose them"""
    result = []
    flagged = []
    traders = {}

    day = -1
    price = -1

    day_threshold = 3
    threshold = 500000

    person = ''
    action = ''
    amount = -1

    datafeed = datafeed.split('\n')

    # Reading through data
    for line in datafeed:

        #print(line)
        trade = line.split("|")

        day = int(trade[0])
        if len(trade) <= 2:
            price = int(trade[1])
        else:
            person = trade[1]
            action = trade[2]
            amount = int(trade[3])

            # Early exit if flagged
            if person in flagged:
                break

            # Add transaction into person
            try:
                traders[person].append((day, action, amount, price))
            except KeyError:
                traders[person] = [(day, action, amount, price)]

        # Iterate for each traders
        for person in traders:
            # Remove transaction if day > 3
            i = 0
            while i < len(traders[person]):
                if day - traders[person][i][0] >= day_threshold:
                    del traders[person][i]
                    continue
                i += 1

            # [day, action, amount, price]
            for transaction in traders[person]:
                if transaction[1] == "BUY":
                    amount = (price - transaction[3]) * transaction[2]
                else:
                    amount = (transaction[3] - price) * transaction[2]

                if (amount >= threshold) and (person not in flagged):
                    flagged.append(person)
                    result.append(str(transaction[0]) + "|" + str(person))

        #print(traders)

    return result

feed1 = """0|1000
0|Shilpa|BUY|30000
0|Will|BUY|50000
0|Tom|BUY|40000
0|Kristi|BUY|15000
1|Kristi|BUY|11000
1|Tom|BUY|1000
1|Will|BUY|19000
1|Shilpa|BUY|25000
2|1500
2|Will|SELL|7000
2|Shilpa|SELL|8000
2|Kristi|SELL|6000
2|Tom|SELL|9000
3|500
38|1000
78|Shilpa|BUY|30000
79|Kristi|BUY|60000
80|1100
81|1200"""

feed2 = """0|20
0|Kristi|SELL|3000
0|Will|BUY|5000
0|Tom|BUY|50000
0|Shilpa|BUY|1500
1|Tom|BUY|1500000
3|25
5|Shilpa|SELL|1500
8|Kristi|SELL|600000
9|Shilpa|BUY|500
10|15
11|5
14|Will|BUY|100000
15|Will|BUY|100000
16|Will|BUY|100000
17|25"""

print catching_insider(feed1)
print catching_insider(feed2)
