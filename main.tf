module "vpc" {
  source             = "./modules/vpc"
  cidr_block         = "10.0.0.0/16"
  public_cidr_block  = "10.0.1.0/24"
  private_cidr_block = "10.0.2.0/24"
  availability_zone  = "us-east-1a"
}

module "security_group" {
  source      = "./modules/security_group"
  name        = "web-sg"
  description = "Allow SSH"
  vpc_id      = module.vpc.vpc_id
}

module "jenkins" {
  source           = "./modules/ec2"
  ami              = data.aws_ami.ubuntu.id
  instance_type    = "t2.medium"
  instance_count   = 1
  subnet_id        = module.vpc.public_subnet_id
  sg_id            = module.security_group.sg_id
  key_name         = "tech4dev"
  role             = "jenkins"
  enable_public_ip = true
}
