variable "s3_bucket_jar" {
  default = "automatictester.co.uk-lightning-aws-lambda-jar"
}
variable "s3_bucket_data" {
  default = "automatictester.co.uk-lightning-aws-lambda"
}
variable "jar_file_name" {
  default = "lightning-aws-lambda.jar"
}
variable "memory" {
  default = "256"
}
variable "timeout" {
  default = "30"
}
