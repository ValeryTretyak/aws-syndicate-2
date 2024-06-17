Get assume role arn
```shell
aws --profile my iam get-role --role-name cmtr-7447aaf5-iam-ar-iam_role-assume
```
result was:
```
{
    "Role": {
        "Path": "/",
        "RoleName": "cmtr-7447aaf5-iam-ar-iam_role-assume",
        "RoleId": "AROAS3MHLZ5ITCPWX2PEB",
        "Arn": "arn:aws:iam::196241772369:role/cmtr-7447aaf5-iam-ar-iam_role-assume",
        "CreateDate": "2024-06-17T13:07:15+00:00",
        "AssumeRolePolicyDocument": {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Sid": "",
                    "Effect": "Allow",
                    "Principal": {
                        "AWS": "arn:aws:iam::196241772369:root"
                    },
                    "Action": "sts:AssumeRole"
                }
            ]
        },
        "MaxSessionDuration": 3600,
        "PermissionsBoundary": {
            "PermissionsBoundaryType": "Policy",
            "PermissionsBoundaryArn": "arn:aws:iam::196241772369:policy/eo_role_boundary"
        },
        "Tags": [
            {
                "Key": "employee",
                "Value": "7447aaf5"
            },
            {
                "Key": "owner",
                "Value": "cmtr"
            },
            {
                "Key": "task_name",
                "Value": "iam_assume_role"
            },
            {
                "Key": "module_name",
                "Value": "iam"
            },
            {
                "Key": "environment",
                "Value": "iam_assume_role"
            }
        ],
        "RoleLastUsed": {}
    }
}
```
Use this role arn in the `sts-policy.json` as principal.
Get readonly role arn and use it in the `allow_assume_role_policy.json` file.

Create policy to allow assuming the role ${readonly_role} 
```shell 
  aws --profile my \
  iam create-policy \
  --policy-name ${prefix}AssumeReadonlyRolePolicy \
  --policy-document file://allow_assume_role_policy.json
```

Attach policy to the role ${assume_role}
```shell
  aws --profile my  \
  iam attach-role-policy \
  --policy-arn arn:aws:iam::196241772369:policy/${prefix}AssumeReadonlyRolePolicy \
  --role-name cmtr-7447aaf5-iam-ar-iam_role-assume
```

Grant full read-only access for the ${readonly_role} role.
Readonly policy arn "arn:aws:iam::aws:policy/ReadOnlyAccess"
```shell
aws --profile my  \
iam attach-role-policy \
--policy-arn arn:aws:iam::aws:policy/ReadOnlyAccess \
--role-name cmtr-7447aaf5-iam-ar-iam_role-readonly
```

Add trust policy to the readonly_role

```shell
  aws  --profile my \
  iam update-assume-role-policy \
  --role-name cmtr-7447aaf5-iam-ar-iam_role-readonly \
  --policy-document file://sts-policy.json
```
