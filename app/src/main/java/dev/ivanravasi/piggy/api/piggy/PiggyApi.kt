package dev.ivanravasi.piggy.api.piggy

import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyRequest
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TokenCreateRequest
import dev.ivanravasi.piggy.api.piggy.bodies.responses.TokenResponse
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.AccountType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Stat
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ListResponse
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.AccountRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.BeneficiaryRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.CategoryRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.ChartRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyVariationRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.RegisterRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransactionRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransferRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PiggyApi {
    @Headers("Accept: application/json")
    @POST("/api/token/create")
    suspend fun token(@Body loginBody: TokenCreateRequest): Response<TokenResponse>

    @Headers("Accept: application/json")
    @POST("/api/token/revoke")
    suspend fun revoke(@Header("Authorization") authHeader: String): Response<TokenResponse>

    @Headers("Accept: application/json")
    @POST("/api/token/register")
    suspend fun register(@Body registerBody: RegisterRequest): Response<TokenResponse>

    @Headers("Accept: application/json")
    @GET("/api/properties")
    suspend fun properties(@Header("Authorization") authHeader: String): Response<ListResponse<Property>>

    @Headers("Accept: application/json")
    @POST("/api/properties")
    suspend fun propertyAdd(@Header("Authorization") authHeader: String, @Body propertyBody: PropertyRequest): Response<ObjectResponse<Property>>

    @Headers("Accept: application/json")
    @PUT("/api/properties/{id}")
    suspend fun propertyUpdate(@Header("Authorization") authHeader: String, @Body propertyBody: PropertyRequest, @Path("id") propertyId: Long): Response<ObjectResponse<Property>>

    @Headers("Accept: application/json")
    @GET("/api/beneficiaries")
    suspend fun beneficiaries(@Header("Authorization") authHeader: String): Response<ListResponse<Beneficiary>>

    @Headers("Accept: application/json")
    @POST("/api/beneficiaries")
    suspend fun beneficiaryAdd(@Header("Authorization") authHeader: String, @Body beneficiaryBody: BeneficiaryRequest): Response<ObjectResponse<Beneficiary>>

    @Headers("Accept: application/json")
    @PUT("/api/beneficiaries/{id}")
    suspend fun beneficiaryUpdate(@Header("Authorization") authHeader: String, @Body beneficiaryBody: BeneficiaryRequest, @Path("id") beneficiaryId: Long): Response<ObjectResponse<Beneficiary>>

    @Headers("Accept: application/json")
    @GET("/api/categories/root")
    suspend fun categoryTrees(@Header("Authorization") authHeader: String): Response<ListResponse<Category>>

    @Headers("Accept: application/json")
    @GET("/api/categories/leaves")
    suspend fun categoryLeaves(@Header("Authorization") authHeader: String): Response<ListResponse<Category>>

    @Headers("Accept: application/json")
    @POST("/api/categories")
    suspend fun categoryAdd(@Header("Authorization") authHeader: String, @Body categoryBody: CategoryRequest): Response<ObjectResponse<Category>>

    @Headers("Accept: application/json")
    @PUT("/api/categories/{id}")
    suspend fun categoryUpdate(@Header("Authorization") authHeader: String, @Body categoryBody: CategoryRequest, @Path("id") categoryId: Long): Response<ObjectResponse<Category>>

    @Headers("Accept: application/json")
    @GET("/api/account_types")
    suspend fun accountTypes(@Header("Authorization") authHeader: String): Response<List<AccountType>>

    @Headers("Accept: application/json")
    @GET("/api/accounts")
    suspend fun accounts(@Header("Authorization") authHeader: String): Response<ListResponse<Account>>

    @Headers("Accept: application/json")
    @POST("/api/accounts")
    suspend fun accountAdd(@Header("Authorization") authHeader: String, @Body accountBody: AccountRequest): Response<ObjectResponse<Account>>

    @Headers("Accept: application/json")
    @PUT("/api/accounts/{id}")
    suspend fun accountUpdate(@Header("Authorization") authHeader: String, @Body accountBody: AccountRequest, @Path("id") accountId: Long): Response<ObjectResponse<Account>>

    @Headers("Accept: application/json")
    @GET("/api/accounts/{id}")
    suspend fun account(@Header("Authorization") authHeader: String, @Path("id") id: Long): Response<ObjectResponse<Account>>

    @Headers("Accept: application/json")
    @GET("/api/categories/{id}")
    suspend fun category(@Header("Authorization") authHeader: String, @Path("id") id: Long): Response<ObjectResponse<Category>>

    @Headers("Accept: application/json")
    @GET("/api/beneficiaries/{id}")
    suspend fun beneficiary(@Header("Authorization") authHeader: String, @Path("id") id: Long): Response<ObjectResponse<Beneficiary>>

    @Headers("Accept: application/json")
    @POST("/api/transactions")
    suspend fun transactionAdd(@Header("Authorization") authHeader: String, @Body transactionBody: TransactionRequest): Response<ObjectResponse<Transaction>>

    @Headers("Accept: application/json")
    @PUT("/api/transactions/{id}")
    suspend fun transactionUpdate(@Header("Authorization") authHeader: String, @Body transactionBody: TransactionRequest, @Path("id") transactionId: Long): Response<ObjectResponse<Transaction>>

    @Headers("Accept: application/json")
    @POST("/api/transfers")
    suspend fun transferAdd(@Header("Authorization") authHeader: String, @Body transferBody: TransferRequest): Response<ObjectResponse<Transfer>>

    @Headers("Accept: application/json")
    @PUT("/api/transfers/{id}")
    suspend fun transferUpdate(@Header("Authorization") authHeader: String, @Body transferBody: TransferRequest, @Path("id") transferId: Long): Response<ObjectResponse<Transfer>>

    @Headers("Accept: application/json")
    @POST("/api/properties/{id}/variations")
    suspend fun variationAdd(@Header("Authorization") authHeader: String, @Path("id") id: Long, @Body variationBody: PropertyVariationRequest): Response<ObjectResponse<Property.PropertyVariation>>

    @Headers("Accept: application/json")
    @GET("/api/budget")
    suspend fun budget(@Header("Authorization") authHeader: String, @Query("year") year: Int): Response<ListResponse<Category>>

    @Headers("Accept: application/json")
    @GET("/api/charts")
    suspend fun charts(@Header("Authorization") authHeader: String): Response<ListResponse<Chart>>

    @Headers("Accept: application/json")
    @POST("/api/charts")
    suspend fun chartAdd(@Header("Authorization") authHeader: String, @Body chartBody: ChartRequest): Response<ObjectResponse<Chart>>

    @Headers("Accept: application/json")
    @PUT("/api/charts/{id}")
    suspend fun chartUpdate(@Header("Authorization") authHeader: String, @Path("id") id: Long): Response<ObjectResponse<Chart>>

    @Headers("Accept: application/json")
    @GET("/api/charts/favorites")
    suspend fun chartsFavorites(@Header("Authorization") authHeader: String): Response<ListResponse<Chart>>

    @Headers("Accept: application/json")
    @GET("/api/stats/{interval}/{filter}/{filter_id}")
    suspend fun statsFiltered(
        @Header("Authorization") authHeader: String,
        @Path("interval") interval: String,
        @Path("filter") filter: String,
        @Path("filter_id") filterId: Long,
        @Query("stat") stat: String
    ): Response<List<Stat>>

    @Headers("Accept: application/json")
    @GET("/api/stats/{interval}")
    suspend fun stats(
        @Header("Authorization") authHeader: String,
        @Path("interval") interval: String,
        @Query("stat") stat: String
    ): Response<List<Stat>>

    @Headers("Accept: application/json")
    @GET("/api/stats/{type}/top")
    suspend fun statsList(
        @Header("Authorization") authHeader: String,
        @Path("type") entityType: String,
        @Query("interval") interval: String,
        @Query("stat") stat: String
    ): Response<List<Stat>>

    @Headers("Accept: application/json")
    @DELETE("/api/{resources}/{id}")
    suspend fun delete(@Header("Authorization") authHeader: String, @Path("id") id: Long, @Path("resources") resources: String)
}