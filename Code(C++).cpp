#include<bits/stdc++.h>
using namespace std;
#define F first
#define S second
#define ignore ignore
#define pb emplace_back
#define mt make_tuple
#define gcd __gcd
// Input
#define in(a) scanf("%d",&a)
#define in2(a,b) scanf("%d%d",&a,&b)
#define in3(a,b,c) scanf("%d%d%d",&a,&b,&c)
#define in4(a,b,c,d) scanf("%d%d%d%d",&a,&b,&c,&d)
#define inp(p) in2(p.F,p.S)
#define llin(a) cin >> a
#define llin2(a,b) cin >> a >> b
#define llin3(a,b,c) cin >> a >> b >> c
#define inl(a) scanf("%lld",&a)
#define read(v,i,n) for(i=0;i<n;i++)in(v[i])
#define twodread(mat,i,j,n,m) rep(i,n){rep(j,m)in(mat[i][j]);}
#define sc(ch) scanf("%c",&ch)
#define sstr(str) scanf("%s",str)
// Output
#define pr(a) printf("%d ",a)
#define pr2(a,b) printf("%d %d\n",a,b)
#define pr3(a,b,c) printf("%d %d %d\n",a,b,c)
#define prp(p) pr2(p.F,p.S)
#define out(a) printf("%d\n",a)
#define outl(a) printf("%lld\n",a)
#define llpr(a) cout << a << " "
#define llpr2(a,b) cout << a << " " << b << "\n"
#define llout(a) cout << a << "\n"
#define pinttwod(mat,i,j,n,m) rep(i,n){rep(j,m)pr(mat[i][j]);   lin;}
#define plltwod(mat,i,j,n,m)  rep(i,n){rep(j,m)llpr(mat[i][j]); lin;}
#define byes printf("YES\n")
#define bno printf("NO\n")
#define yes printf("Yes\n")
#define no printf("No\n")
#define lin printf("\n")
#define test(q) cout << "Case #" << q << ": "
// Iterator
#define rep(i,n) for(i=0;i<n;++i)
#define fone(i,n) for(i=1;i<=n;++i)
#define rrep(i,n) for(i=n-1;i>=0;--i)
#define lp(i,a,b) for(i=a;i<b;++i)
#define rlp(i,a,b) for(i=a;i>=b;--i)
#define all(vec) vec.begin(),vec.end()
#define lower(v,k) lower_bound(v.begin(),v.end(),k)-v.begin()
#define upper(v,k) upper_bound(v.begin(),v.end(),k)-v.begin()
#define tf(mytuple) get<0>(mytuple)
#define ts(mytuple) get<1>(mytuple)
#define tt(mytuple) get<2>(mytuple)
// function
#define sz(x) (int)x.size()
#define inrange(i,a,b) (i>=a && i<=b)
#define FLUSH fflush(stdout)
#define precision(x) cout << setprecision(x) << fixed
#define remax(a,b) a=max(a,b)
#define remin(a,b) a=min(a,b)
#define middle() ((l+h)/2)
#define lchild(p) 2*p
#define rchild(p) 2*p+1
#define lseg l,m,2*p
#define rseg m+1,h,2*p+1
#define bhardo(mat,i,j,n,m,t) rep(i,n){rep(j,m)mat[i][j]=t;}
#define baselog2(n) __lg(n)
#define numberofbit(n) (32-__builtin_clz(n))
#define numberofbitll(n) (64-__builtin_clzll(n))
#define onbitcount(x)  __builtin_popcount(x)
#define onbitcountll(x)  __builtin_popcountll(x)
#define biton(mask,i)  ((mask>>i)&1)
#define bitoff(mask,i) (!((mask>>i)&1))
#define toggle(mask,i) (mask^=(1<<(i)))
#define raise(i) (1<<(i))
#define mul(p,q) ((ll)(p)*(ll)(q))
// Debug
#define dbg(v,i,n) for(i=0;i<n;i++)pr(v[i]); lin
#define ck printf("continue\n")
#define debug(args...) { string _s = #args; replace(_s.begin(), _s.end(), ',', ' '); stringstream _ss(_s); istream_iterator<string> _it(_ss); err(_it, args); }
void err(istream_iterator<string> it) {}
template<typename T, typename... Args>
void err(istream_iterator<string> it, T a, Args... args)
{
	cerr << *it << " = " << a << "\n";
	err(++it, args...);
}
// Data Type
#define ll long long int
#define ii pair<int,int>
#define pli pair<ll,int>
#define lii pair<ll,ll>
#define triple tuple<int,int,int>
#define vi vector<int>
#define vll vector<ll>
#define vii vector<pair<int,int> >
#define vvi vector<vector<int> >
#define viii vector<pair<pair<int,int>,int> >
#define vvii vector<vector<pair<int,int> > >
// Constant
const double PI = acos(-1);
const double eps = (1e-9);
const ll INF = 2e18;
const int M = (1e9)+7;
//const int M= 998244353;
const int N = 53;  // check the limit, man
/**
 Have you worked on Simplification of Idea?
 Ok! let's code, check whether it is correct or not?
*/
struct bipartite_matching // Hopcroft-Karp Algorithm works in O(E*sqrt(V))
{
    int n,m,root=0;
    // n nodes on left side numbered [1,n] and m nodes on right side numbered [n+1,n+m]
    vector<vector<int> > graph;
    vector<int> lev,match;
    bipartite_matching(int l,int r) : n(l),m(r),graph(l+1),lev(l+1),match(l+r+1) {}
    void add_edge(int a,int b) // 1-indexing, a->[1,n] and b->[1,m]
    {
        graph[a].pb(n+b);
    }
    bool bfs()
    {
        queue<int> pq;
        fill(all(lev),M);
        for(int i=1;i<=n;i++)if(match[i]==root)lev[i]=0,pq.push(i);
        while(!pq.empty())
        {
            int s=pq.front(); pq.pop();
            for(auto a : graph[s])
                if(lev[match[a]]==M)lev[match[a]]=lev[s]+1,pq.push(match[a]);
        }
        return (lev[root]!=M);
    }
    bool dfs(int s)
    {
        if(s==root)return true;
        for(auto a : graph[s])
            if(lev[match[a]]==lev[s]+1 && dfs(match[a]))
                return match[s]=a,match[a]=s,true;
        return lev[s]=M,false;
    }
    int max_matching() // match array based on [1,n+m] so keep in mind
    {
        fill(all(match),root);
        int ans=0;
        while(bfs())
            for(int i=1;i<=n;i++)
                if(match[i]==root && dfs(i))ans++;
        return ans;
    }
};
ii v[N][N];
char ans[N][N];
int I[4][2]={{1,0},{-1,0},{0,1},{0,-1}};
char ar[4]={'U','D','L','R'};
void call()
{
    out(-1);
    exit(0);
}
void solve()
{
    int n,i,j,a,b,x,y,c,k;
    set<ii> st;
    in(n);
    in4(c,a,b,x);
    fone(i,n)
    {
        fone(j,n)
            inp(v[i][j]),st.insert(v[i][j]);
    }
    bipartite_matching space(n*n,n*n);
    for(auto itr=st.begin();itr!=st.end();++itr)
    {
        tie(x,y)=(*itr);
        rep(k,4)
        {
            a=x+I[k][0],b=y+I[k][1];
            if(st.find({a,b})==st.end())
                continue;
            if((x+y)&1)
                space.add_edge(n*a+b-n,n*x+y-n);
            else
                space.add_edge(n*x+y-n,n*a+b-n);
        }
    }
    space.max_matching();
    fone(i,n)
    {
        fone(j,n)
        {
            if(space.match[n*i+j-n]==0)
                continue;
            k=space.match[n*i+j-n]-n*n;
            x=((k-1)/n)+1,y=((k-1)%n)+1;
            rep(k,4)
            {
                a=x+I[k][0],b=y+I[k][1];
                if(a==i && b==j)
                    ans[i][j]=ar[k],ans[x][y]=ar[k^1];
            }
        }
    }
    for(auto itr=st.begin();itr!=st.end();++itr)
    {
        tie(x,y)=(*itr);
        if(ans[x][y]==0)
            call();
        queue<ii> pq;
        pq.push({x,y});
        while(!pq.empty())
        {
            tie(a,b)=pq.front(),pq.pop();
            rep(k,4)
            {
                i=a+I[k][0],j=b+I[k][1];
                if(v[i][j]==make_pair(x,y) && ans[i][j]==0)
                    ans[i][j]=ar[k],pq.push({i,j});
            }
        }
    }
    fone(i,n)
    {
        fone(j,n)
            if(ans[i][j]==0)call();
    }
    llout(n*n*c);
    fone(i,n)
    {
        fone(j,n)
            cout << ans[i][j];
        lin;
    }
}
void starting()
{

}
int main()
{
    int t=1;
//    in(t);
    starting();
    while(t--)
        solve();
}





