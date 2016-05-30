echo "Please enter the Name of upload input: "
read input_variable
git add .
git commit -m "$input_variable"
git push origin master