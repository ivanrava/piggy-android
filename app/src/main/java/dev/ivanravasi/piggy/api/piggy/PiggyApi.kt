package dev.ivanravasi.piggy.api.piggy

import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyRequest
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TokenCreateRequest
import dev.ivanravasi.piggy.api.piggy.bodies.responses.TokenResponse
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.AccountType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ListResponse
import dev.ivanravasi.piggy.api.piggy.bodies.meta.ObjectResponse
import dev.ivanravasi.piggy.api.piggy.bodies.requests.AccountRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.BeneficiaryRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.CategoryRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransactionRequest
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransferRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
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
    @GET("/api/properties")
    suspend fun properties(@Header("Authorization") authHeader: String): Response<ListResponse<Property>>

    @Headers("Accept: application/json")
    @POST("/api/properties")
    suspend fun propertyAdd(@Header("Authorization") authHeader: String, @Body propertyBody: PropertyRequest): Response<ObjectResponse<Property>>

    @Headers("Accept: application/json")
    @GET("/api/beneficiaries")
    suspend fun beneficiaries(@Header("Authorization") authHeader: String): Response<ListResponse<Beneficiary>>

    @Headers("Accept: application/json")
    @POST("/api/beneficiaries")
    suspend fun beneficiaryAdd(@Header("Authorization") authHeader: String, @Body beneficiaryBody: BeneficiaryRequest): Response<ObjectResponse<Beneficiary>>

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
    @GET("/api/account_types")
    suspend fun accountTypes(@Header("Authorization") authHeader: String): Response<List<AccountType>>

    @Headers("Accept: application/json")
    @GET("/api/accounts")
    suspend fun accounts(@Header("Authorization") authHeader: String): Response<ListResponse<Account>>

    @Headers("Accept: application/json")
    @POST("/api/accounts")
    suspend fun accountAdd(@Header("Authorization") authHeader: String, @Body accountBody: AccountRequest): Response<ObjectResponse<Account>>

    @Headers("Accept: application/json")
    @GET("/api/accounts/{id}")
    suspend fun account(@Header("Authorization") authHeader: String, @Path("id") id: Long): Response<ObjectResponse<Account>>

    @Headers("Accept: application/json")
    @POST("/api/transactions")
    suspend fun transactionAdd(@Header("Authorization") authHeader: String, @Body transactionBody: TransactionRequest): Response<ObjectResponse<Transaction>>

    @Headers("Accept: application/json")
    @POST("/api/transfers")
    suspend fun transferAdd(@Header("Authorization") authHeader: String, @Body transferBody: TransferRequest): Response<ObjectResponse<Transfer>>

    @Headers("Accept: application/json")
    @GET("/api/budget")
    suspend fun budget(@Header("Authorization") authHeader: String, @Query("year") year: Int): Response<ListResponse<Category>>
}