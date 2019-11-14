<!DOCTYPE html>
<html lang="en">
	<head>
		<title>index</title>
	</head>
	<body>
		<form action="mqTest/sendDirectMsg" method="post">
        	<input type="submit" value="mq发送direct消息" />
    	</form>
    	<br/>
    	<form action="mqTest/sendTopicMsg" method="post">
        	<input type="submit" value="mq发送topic消息" />
    	</form>
    	<br/>
    	<form action="mqTest/sendFanoutMsg" method="post">
        	<input type="submit" value="mq发送fanout消息" />
    	</form>
    	
	</body>
</html>

