resource "aws_iam_role" "ec2_role" {
  name = "ec2_role_ec2"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ec2.amazonaws.com"
      }
    }]
  })
}


resource "aws_iam_instance_profile" "ec2_profile" {
  name = "ec2_profile_ec2"
  role = aws_iam_role.ec2_role.name
}
