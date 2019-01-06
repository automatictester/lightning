terraform {
  backend "s3" {
    bucket             = "automatictester.co.uk-lightning-aws-lambda-tf-state"
    key                = "lightning-lambda.tfstate"
    region             = "eu-west-2"
  }
}

provider "aws" {
  region               = "eu-west-2"
}

resource "aws_s3_bucket" "jar" {
  bucket               = "${var.s3_bucket_jar}"
  acl                  = "private"
}

resource "aws_s3_bucket_object" "jar" {
  bucket               = "${aws_s3_bucket.jar.bucket}"
  key                  = "${var.jar_file_name}"
  source               = "${path.module}/../target/${var.jar_file_name}"
  etag                 = "${md5(file("${path.module}/../target/${var.jar_file_name}"))}"
}

resource "aws_s3_bucket" "data" {
  bucket               = "${var.s3_bucket_data}"
  acl                  = "private"
  force_destroy        = true
  lifecycle_rule {
    id = "Delete all objects after 1 day"
    enabled = true
    expiration {
      days = 1
    }
  }
}

resource "aws_iam_role" "lightning_role" {
  name                 = "LightningRole"
  description          = "Allow Lightning to use S3 and CloudWatch Logs."
  assume_role_policy   = "${file("iam-policy/assume-role-policy.json")}"
}

resource "aws_iam_policy" "s3_use" {
  name                 = "LightningUseS3Bucket"
  path                 = "/"
  description          = "Lightning - use S3 bucket"
  policy               = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action":
            [
              "s3:PutObject",
              "s3:GetObject"
            ],
            "Effect": "Allow",
            "Resource": "arn:aws:s3:::${aws_s3_bucket.data.bucket}/*"
        }
    ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "s3_data_access_policy" {
  role                 = "${aws_iam_role.lightning_role.name}"
  policy_arn           = "${aws_iam_policy.s3_use.arn}"
}

resource "aws_iam_role_policy_attachment" "cloudwatch_access_policy" {
  role                 = "${aws_iam_role.lightning_role.name}"
  policy_arn           = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

resource "aws_lambda_function" "lightning" {
  function_name                  = "Lightning"
  handler                        = "uk.co.automatictester.lightning.lambda.LightningHandler"
  runtime                        = "java8"
  s3_bucket                      = "${aws_s3_bucket.jar.bucket}"
  s3_key                         = "${var.jar_file_name}"
  source_code_hash               = "${base64sha256(file("${path.module}/../target/${var.jar_file_name}"))}"
  role                           = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:role/${aws_iam_role.lightning_role.name}"
  memory_size                    = "${var.memory}"
  timeout                        = "${var.timeout}"
  reserved_concurrent_executions = "1"
}
