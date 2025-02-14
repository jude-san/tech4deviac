terraform {
  required_providers {
    ## ...
    aws = {
      version = "~> 5.53.0"
      source  = "hashicorp/aws"
    }

  }
}


## specifying provider configurations
provider "aws" {
  region = "eu-north-1"
  # profile = tech4dev

}
