``` shell
cd task04aws/
```

```shell
export prefix=cmtr-7447aaf5-
```

```shell
export iam_role=cmtr-7447aaf5-iam-pela-iam_role
```

```shell
export s3_bucket=cmtr-7447aaf5-iam-pela-bucket-1-5575918
```

```shell
 export s3_default_bucket =cmtr-7447aaf5-iam-pela-bucket-2-5575918
```

```shell
aws --profile my \
> iam create-policy \
>     --policy-name ${prefix}ListBucketCustomPolicy \
>     --policy-document file://policyAListBuckets.json
```
response
```
{
    "Policy": {
        "PolicyName": "cmtr-7447aaf5-ListBucketCustomPolicy",
        "PolicyId": "ANPAS3MHLZ5IR4RPDDHH5",
        "Arn": "arn:aws:iam::196241772369:policy/cmtr-7447aaf5-ListBucketCustomPolicy",
        "Path": "/",
        "DefaultVersionId": "v1",
        "AttachmentCount": 0,
        "PermissionsBoundaryUsageCount": 0,
        "IsAttachable": true,
        "CreateDate": "2024-06-14T15:29:06+00:00",
        "UpdateDate": "2024-06-14T15:29:06+00:00"
    }
}
```

```shell
aws --profile my  \
> iam attach-role-policy \
> --policy-arn arn:aws:iam::196241772369:policy/cmtr-7447aaf5-ListBucketCustomPolicy \
> --role-name ${iam_role}
```

Create bucket policy file b_policy.json
Pay attention that resources for object related permissions should be `<bucket>/*`
and for bucket related permissions just `<bucket>'
```shell
aws s3api put-bucket-policy --bucket ${s3_bucket} --policy file://b_policy.json --profile my
```