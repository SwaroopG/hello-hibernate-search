# hibernate-sqlite-search
* Pull the project.
* Add gradle nature to the project in eclipse.
* Run the **MainApplication** or **gradle run**

## Issues
* Some searches ex. "big bang", run into the following problem as SQLite has a limit on the number of query variables (999).

		[SQLITE_ERROR] SQL error or missing database (too many SQL variables)

## Reference:
http://www.srccodes.com/p/article/8/Full-Text-Hibernate-Lucene-Search-Hello-World-Example-Using-Maven-and-SQLite


## CI Details
[![Build Status][1]][2]

[1]: https://secure.travis-ci.org/SwaroopG/hibernate-sqlite-search.png
[2]: http://www.travis-ci.org/SwaroopG/hibernate-sqlite-search
