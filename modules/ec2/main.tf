module "iam" {
  source = "../iam"
}

resource "aws_instance" "web" {
  count                  = var.instance_count
  ami                    = var.ami
  instance_type          = var.instance_type
  subnet_id              = var.subnet_id
  vpc_security_group_ids = [var.sg_id]
  iam_instance_profile   = modules.iam.aws_iam_instance_profile.ec2_profile.name

  tags = {
    Name = "web-${count.index}"
  }
}



