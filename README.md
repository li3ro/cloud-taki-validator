cloud-taki-validator
====================

On Cloud Taki Validator project is hosted on Google App Engine cloud.
you can direcly POST to it using the following example curl:
curl -d "inpack_type=ONE" -d "inpack_color=BLUE" -d "pc1_type=TAKI" -d "pc1_color=BLUE" -d "pc2_type=FOUR" -d "pc2_color=BLUE" -d "pc3_type=FOUR" -d "pc3_color=RED" http://taki-validator.appspot.com/posthere

or this invalid one:
curl -d "inpack_type=ONE" -d "inpack_color=BLUE" -d "pc1_type=TAKI" -d "pc1_color=YELLOW" -d "pc2_type=FOUR" -d "pc2_color=BLUE" -d "pc3_type=NINE" -d "pc3_color=BLUE" http://taki-validator.appspot.com/posthere



