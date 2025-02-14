terraform {
  required_providers {
    ## ...
    aws = {
      source = "hashicorp/aws"
    }

  }
}


## specifying provider configurations
provider "aws" {
  region = "eu-north-1"
  # profile = tech4dev

}
