provider "aws" {
    region = "eu-west-2"
}

terraform {
    backend "s3" {
    bucket = "automatictester.co.uk-lightning-aws-lambda-tf-state"
    key    = "lightning-lambda.tfstate"
    region = "eu-west-2"
    }
}

resource "aws_lambda_function" "lightning_ci" {

    function_name = "Lightning"
    handler = "uk.co.automatictester.lightning.lambda.LightningHandler"
    runtime = "java8"
    s3_bucket = "automatictester.co.uk-lightning-aws-lambda-jar"
    s3_key = "lightning-aws-lambda.jar"
    role = "arn:aws:iam::574377821355:role/LightningLambdaRole"
    memory_size = "${var.memory}"
    timeout = "${var.timeout}"
}
