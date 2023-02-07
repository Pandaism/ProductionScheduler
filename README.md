# ProductionScheduler

ProductionScheduler is a software to extract Sales Order data from Expandable ERP and display this data on a weekly table to assist with scheduling and tracking orders submitted and shipped.

## Installation
Ensure your system have JRE 11 

An installation is provided on within the Release tab on this project page.


## Usage

Install the jar file and run the jar using:
```
java -jar ProductionScheduler-x.x-SNAPSHOT.jar
```

Populate the created settings.properties with the proper credentials to establish connection Expandable:
```
db.user=user
db.database=database
db.password=password
db.url=ip address
db.wsid=wsid
```

Populate the created shippable_items.txt with part numbers you want to view:
```
PID_1
PID_2
PID_3
etc
```

## Contributing

For major changes, please open an issue first
to discuss what you would like to change.

## License

Open-source
