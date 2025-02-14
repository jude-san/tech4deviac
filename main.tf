module "vpc" {
  source            = "./modules/vpc"
  cidr_block        = "10.0.0.0/16"
  subnet_cidr_block = "10.0.1.0/24"
  availability_zone = "eu-north-1a"
}

module "security_group" {
  source      = "./modules/security_group"
  name        = "web-sg"
  description = "Allow SSH"
  vpc_id      = module.vpc.vpc_id
}

module "iam" {
  source = "./modules/iam"
}

module "ec2" {
  source         = "./modules/ec2"
  ami            = "ami-12345678"
  instance_type  = "t2.micro"
  instance_count = 3
  subnet_id      = module.vpc.subnet_id
  sg_id          = module.security_group.sg_id
  iam_role       = module.iam.role_name
}

resource "null_resource" "example" {
  provisioner "local-exec" {
    command = "echo 'Hello, World!'"
  }
}

resource "null_resource" "name" {
  provisioner "local-exec" {
    command = "echo 'Hello, World!'"
  }
}


