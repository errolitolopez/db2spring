== ${className.toPascalCase().toSentenceCase()} Management

=== ${className.toPascalCase().toSentenceCase()} - Create

==== Description
Create a new ${className.toLowerSentenceCase()} resource.

==== Method
`POST`

==== Permission

==== Specification
operation::${apiName.toKebabCase()}/create[snippets='curl-request,http-request,http-response,request-fields,response-fields']


=== ${className.toPascalCase().toSentenceCase()} - Update

==== Description
Update an existing ${className.toLowerSentenceCase()} resource.

==== Method
`PUT`

==== Permission

==== Specification
operation::${apiName.toKebabCase()}/update[snippets='curl-request,http-request,http-response,path-parameters,request-fields,response-fields']


=== ${className.toPascalCase().toSentenceCase()} - Details

==== Description
Get detailed information for a specific ${className.toLowerSentenceCase()} resource by ID.

==== Method
`GET`

==== Permission

==== Specification
operation::${apiName.toKebabCase()}/details[snippets='curl-request,http-request,http-response,path-parameters,response-fields']


=== ${className.toPascalCase().toSentenceCase()} - List

==== Description
${className.toLowerSentenceCase()} - List / Search

==== Method
`GET`

==== Permission

==== Specification
operation::${apiName.toKebabCase()}/search[snippets='curl-request,http-request,http-response,request-parameters,response-fields']

=== ${className.toPascalCase().toSentenceCase()} - Delete

==== Description
Delete a specific ${className.toLowerSentenceCase()} resource by ID.

==== Method
`DELETE`

==== Permission

==== Specification
operation::${apiName.toKebabCase()}/delete[snippets='curl-request,http-request,http-response,path-parameters,response-fields']