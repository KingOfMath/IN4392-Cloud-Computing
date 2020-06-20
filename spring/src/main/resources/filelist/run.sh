#!/bin/bash
#for i in 1 2 3 4 5
#do

#sed -i "#!/usr/bin/env python3 \n" /home/ubuntu/*.py

declare -i i=0
ID2=0
while :
do	
	#python *.py
	mv *.py process.py
	FILE=/home/ubuntu/process.py

	sleep 1

		if test -f "$FILE"; then

			#python3 process.py
			#chmod +x /home/ubuntu/*.py

			ID1=$(cat /sys/hypervisor/uuid)
			ID="${ID1}_${ID2}"

			#python3 /home/ubuntu/*.py
			(time (python3 process.py)) &> log.txt
			
			sleep 1

			#cp whale1-*.jpg "${ID}".jpg
			mv log.txt "${ID}".txt

			rm whale1.jpg

			#rm whale1-*.jpg
			mv *.jpg "${ID}".jpg

			if test -f whale.gif; then
				mv *.gif "${ID}".gif
				aws s3 cp "${ID}".gif s3://lyximager
				rm *.gif
			fi

			if test -f *.jpg; then
				mv *.jpg "${ID}".jpg
				aws s3 cp "${ID}".jpg s3://lyximager
				rm *.jpg
			fi

			if test -f *.png; then
				mv *.png "${ID}".png
				aws s3 cp "${ID}".png s3://lyximager
				rm *.png
			fi


			#rm log.txt
			#aws s3 cp "${ID}".jpg s3://lyximager
			aws s3 cp "${ID}".txt s3://lyxlogger
			#echo running1
			rm *.py
			rm *.txt
			#rm *.jpg
			#rm *.gif

			((ID2=ID2+1))

			sleep 1

			fi


	#echo running2		
done
#sudo shutdown -h now
