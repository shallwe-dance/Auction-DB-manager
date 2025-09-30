all:
	javac Auction.java

run:
	java -Duser.timezone="Asia/Seoul" -cp .:./postgresql-42.6.0.jar Auction s20313974 thankyou
