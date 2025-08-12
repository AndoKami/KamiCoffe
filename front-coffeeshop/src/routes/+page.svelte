<div class="container" role="button" tabindex="0" on:click={() => console.log('Container clicked')} on:keydown={(e) => { if (e.key === 'Enter' || e.key === ' ') console.log('Container clicked'); }}>
  <div class="top"></div>
  <div class="bottom"></div>
  <div class="center">
    <h2>Please Sign In</h2>
    <input type="email" placeholder="email" aria-label="Email address" />
    <input type="password" placeholder="password" aria-label="Password" />
    <h2>{' '}</h2> <!-- Remplace &nbsp; par un espace ou texte si nécessaire -->
  </div>
</div>

<style>
</style>